package com.app;

import com.app.palette.FilterItem;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import static com.app.OpenCvUtils.*;

/**
 *
 * @author ferdy
 */
public class Main extends JFrame {
    private Camera panelCamera;
    private volatile Image image = null;

    private volatile boolean running = true;
    
    private VideoCapture videoCapture = new VideoCapture(0);
    private final Mat frame = new Mat();

    private final Mat matGray = new Mat();
    private final Mat matSepia = new Mat();
    private final Mat matInvert = new Mat();
    private final Mat matColorMap = new Mat();
    
    private List<PanelFilterPreview> filterPreviews;
    private List<FilterItem> filterItems;
    private FilterItem selectedFilter;

    private final PanelCustomEffects panelCustomEffects;
    private final PanelBottomControls panelBottomControls;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Camera");
        
        panelCamera = new Camera(this);
        panelContainerCamera.add(panelCamera, BorderLayout.CENTER);

        panelBottomControls = new PanelBottomControls();
        panelSouth.add(panelBottomControls, BorderLayout.CENTER);

        panelBottomControls.getToggleButtonGrid().addActionListener(e -> {
            boolean isSelected = panelBottomControls.getToggleButtonGrid().isSelected();

            panelCamera.setShowGrid(isSelected);
        });
        panelBottomControls.getButtonCapture().addActionListener(e -> {
            String opcDelay = (String) panelBottomControls.getComboBoxTimer().getSelectedItem();
            int delay = switch (opcDelay) {
                case "3 segundos" -> 3000;
                case "5 segundos" -> 5000;
                case "10 segundos" -> 10000;
                default -> 0;
            };

            takePicture(delay);
        });


        filterItems = new ArrayList<>();
        filterItems.add(new FilterItem("Normal", null));
        filterItems.add(new FilterItem("Blanco y negro", null));
        filterItems.add(new FilterItem("Sepia", null));
        filterItems.add(new FilterItem("Invertir", null));
        filterItems.add(new FilterItem("Mapa de calor", null));

        filterPreviews = new ArrayList<>();
        for(FilterItem item : filterItems) {
            PanelFilterPreview panelFilterPreview = new PanelFilterPreview(item);
            panelFilterPreview.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selectedFilter = panelFilterPreview.getFilterItem();
                    updateSelectionUI();
                }
            });
            filterPreviews.add(panelFilterPreview);
            panelFiltersContainer.add(panelFilterPreview);
        }
        
        selectedFilter = filterItems.getFirst();
        updateSelectionUI();

        panelCustomEffects = new PanelCustomEffects();
        tabbedPane.addTab("Personalizado", panelCustomEffects);
        
        if (videoCapture.read(frame) && !frame.empty()) {
            // Esto "calienta" los FilterItem con imágenes antes de que el usuario interactúe.
            updateFilterPreviews();
        }
        startCameraLoop();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContainerCamera = new JPanel();
        panelSouth = new JPanel();
        jPanel2 = new JPanel();
        tabbedPane = new JTabbedPane();
        jScrollPane1 = new JScrollPane();
        panelFiltersContainer = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panelContainerCamera.setBackground(new Color(255, 51, 51));
        panelContainerCamera.setMinimumSize(new Dimension(500, 500));
        panelContainerCamera.setPreferredSize(new Dimension(700, 500));
        panelContainerCamera.setLayout(new BorderLayout());

        panelSouth.setPreferredSize(new Dimension(384, 50));
        panelSouth.setLayout(new BorderLayout());
        panelContainerCamera.add(panelSouth, BorderLayout.SOUTH);

        panelContainerCamera.setSize(new Dimension(300, 500));

        getContentPane().add(panelContainerCamera, BorderLayout.CENTER);

        jPanel2.setPreferredSize(new Dimension(250, 615));

        tabbedPane.setToolTipText("");

        panelFiltersContainer.setLayout(new GridLayout(0, 1));
        jScrollPane1.setViewportView(panelFiltersContainer);

        tabbedPane.addTab("Filtros", null, jScrollPane1, "");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        getContentPane().add(jPanel2, BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(WindowEvent evt) {
        running = false;
    }

    private void updateSelectionUI() {
        for(PanelFilterPreview panelFilterPreview : filterPreviews) {
            panelFilterPreview.setSelected(selectedFilter == panelFilterPreview.getFilterItem());
        }
    }

    private void startCameraLoop() {
        new Thread(() -> {
            while (running && videoCapture.isOpened()) {
                try {
                    if(!videoCapture.read(frame) || frame.empty()) {
                        break;
                    }

                    Mat filterFrame = new Mat();
                    int activeTab = tabbedPane.getSelectedIndex();

                    if(activeTab == 0) {
                        filterFrame = filterTabIsSelected(filterFrame);
                        updateFilterPreviews();
                    }
                    else if(activeTab == 1) {
                        int brightness = panelCustomEffects.getSliderBrightness().getValue();
                        double contrast = (double) panelCustomEffects.getSliderContrast().getValue() / 100;
                        int blur = (int) panelCustomEffects.getSpinnerBlur().getValue();
                        boolean invert = panelCustomEffects.getCheckBoxInvert().isSelected();

                        filterFrame = applyCustomFilter(frame, brightness, contrast, blur, invert);
                    }

                    image = matToImage(filterFrame);

                    SwingUtilities.invokeLater(() -> {
                        panelCamera.setImage(image);
                        panelCamera.repaint();
                    });

                    Thread.sleep(33);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            videoCapture.release();
        }, "CameraThread").start();
    }

    private Mat filterTabIsSelected(Mat filterFrame) {
        if(selectedFilter.getName().equals("Blanco y negro")) {
            Imgproc.cvtColor(frame, filterFrame, Imgproc.COLOR_BGR2GRAY);
        }
        else if(selectedFilter.getName().equals("Sepia")) {
            applySepiaFilter(frame, filterFrame);
        }
        else if(selectedFilter.getName().equals("Invertir")) {
            Core.bitwise_not(frame, filterFrame);
        }
        else if(selectedFilter.getName().equals("Mapa de calor")) {
            Imgproc.applyColorMap(frame, filterFrame, Imgproc.COLORMAP_JET);
        }
        else {
            filterFrame = frame;
        }
        return filterFrame;
    }

    private void updateFilterPreviews() {
        //normal
        filterPreviews.getFirst().setPreviewImage(matToImage(frame));

        //black and white
        Imgproc.cvtColor(frame, matGray, Imgproc.COLOR_BGR2GRAY);
        filterPreviews.get(1).setPreviewImage(matToImage(matGray));

        //sepia
        applySepiaFilter(frame, matSepia);
        filterPreviews.get(2).setPreviewImage(matToImage(matSepia));

        //invert
        Core.bitwise_not(frame, matInvert);
        filterPreviews.get(3).setPreviewImage(matToImage(matInvert));

        //colormap
        Imgproc.applyColorMap(frame, matColorMap, Imgproc.COLORMAP_JET);
        filterPreviews.get(4).setPreviewImage(matToImage(matColorMap));
    }

    private void takePicture(int timer) {
        new Thread(() -> {
            try {
                panelBottomControls.getButtonCapture().setEnabled(false);

                int seconds = timer / 1000;
                if(seconds > 0) {
                    for(int i = seconds; i > 0; i--) {
                        panelCamera.setCountdownText(String.valueOf(i));
                        Thread.sleep(1000);
                    }
                }

                panelCamera.setCountdownText("");
                Thread.sleep(100);

                Image imageToSave = image;
                if(imageToSave == null) {
                    throw new IOException("Can't take picture");
                }

                BufferedImage bufferedImage = new BufferedImage(
                        image.getWidth(null),
                        image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D graphics = bufferedImage.createGraphics();
                graphics.drawImage(imageToSave, 0, 0, null);
                graphics.dispose();

                final File[] fileResult = new File[1];
                SwingUtilities.invokeAndWait(() -> {
                    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + File.separator + "Pictures");
                    fileChooser.setDialogTitle("Guardar foto");
                    fileChooser.setSelectedFile(new File("captura_" + System.currentTimeMillis() + ".png"));
                    if(fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                        fileResult[0] = fileChooser.getSelectedFile();
                    }

                    if(fileResult[0] != null) {
                        try {
                            ImageIO.write(bufferedImage, "png", fileResult[0]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Main.this, "Can't take picture");
                });
                e.printStackTrace();
            } finally {
                panelBottomControls.getButtonCapture().setEnabled(true);
            }
        }).start();

    }
    
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JPanel panelContainerCamera;
    private JPanel panelFiltersContainer;
    private JPanel panelSouth;
    private JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
