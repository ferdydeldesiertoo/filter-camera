package com.app;

import com.app.palette.FilterItem;
import com.app.palette.FilterItemRenderer;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import static com.app.OpenCvUtils.matToImage;

/**
 *
 * @author ferdy
 */
public class Main extends javax.swing.JFrame {
    private Camera panelCamera;
    private Image image = null;
    
    private VideoCapture videoCapture = new VideoCapture(0);
    private MatOfByte mem = new MatOfByte();
    private Mat frame = new Mat();
    
    private List<PanelFilterPreview> filterPreviews;

    private List<FilterItem> filterItems;
    private FilterItem selectedFilter;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Camera");
        
        panelCamera = new Camera(this);
        panelContainerCamera.add(panelCamera, BorderLayout.CENTER);

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
        
        if (videoCapture.read(frame) && !frame.empty()) {
            // Esto "calienta" los FilterItem con imágenes antes de que el usuario interactúe.
            updateFilterPreviews();
        }
        clock.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContainerCamera = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelFiltersContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panelContainerCamera.setBackground(new java.awt.Color(255, 51, 51));
        panelContainerCamera.setMinimumSize(new java.awt.Dimension(500, 500));
        panelContainerCamera.setPreferredSize(new java.awt.Dimension(700, 500));
        panelContainerCamera.setLayout(new java.awt.BorderLayout());

        panelContainerCamera.setSize(new java.awt.Dimension(300, 500));

        getContentPane().add(panelContainerCamera, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 615));

        jTabbedPane1.setToolTipText("");

        panelFiltersContainer.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane1.setViewportView(panelFiltersContainer);

        jTabbedPane1.addTab("Filtros", null, jScrollPane1, "");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {                                  
        try {
            clock.stop();
        } catch (Exception e) {
        }
    }

    private void updateSelectionUI() {
        for(PanelFilterPreview panelFilterPreview : filterPreviews) {
            panelFilterPreview.setSelected(selectedFilter == panelFilterPreview.getFilterItem());
        }
    }
  
    Timer clock = new Timer(70, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(!videoCapture.read(frame) || frame.empty()) {
                    clock.stop();
                    return; 
                }
         
                Mat filterFrame = new Mat();
                if(selectedFilter.getName().equals("Blanco y negro")) {
                    Imgproc.cvtColor(frame, filterFrame, Imgproc.COLOR_BGR2GRAY);
                }
                else if(selectedFilter.getName().equals("Sepia")) {
                    filterFrame = OpenCvUtils.applySepiaFilter(frame);
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
                      
                Imgcodecs.imencode(".bmp", filterFrame, mem);
                image = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                    
                panelCamera.setImage(image);
                panelCamera.updateUI();
                    
                updateFilterPreviews();
                
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    });
   
    private void updateFilterPreviews() {
        //normal
        filterPreviews.getFirst().setPreviewImage(matToImage(frame));

        //black and white
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        filterPreviews.get(1).setPreviewImage(matToImage(grayFrame));

        //sepia
        Mat sepiaFrame = OpenCvUtils.applySepiaFilter(frame);
        filterPreviews.get(2).setPreviewImage(matToImage(sepiaFrame));

        //invert
        Mat invertFrame = new Mat();
        Core.bitwise_not(frame, invertFrame);
        filterPreviews.get(3).setPreviewImage(matToImage(invertFrame));

        //colormap
        Mat colorMapFrame = new Mat();
        Imgproc.applyColorMap(frame, colorMapFrame, Imgproc.COLORMAP_JET);
        filterPreviews.get(4).setPreviewImage(matToImage(colorMapFrame));
    }
    
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelContainerCamera;
    private javax.swing.JPanel panelFiltersContainer;
    // End of variables declaration//GEN-END:variables
}
