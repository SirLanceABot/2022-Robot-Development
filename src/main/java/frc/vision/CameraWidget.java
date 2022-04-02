package frc.vision;
//FIXME: add class and constructor identifiers
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class CameraWidget {

    public ShuffleboardTab cameraTab;
    public String name;
    public int column;
    public int row;
    public int width;
    public int height;
    
    // Name Type Default Value Notes
    // Show crosshair Boolean true Show or hide a cross-hair on the image
    // Crosshair color Color "white" Can be a string or a rgba integer
    // Show controls Boolean true Show or hide the stream controls
    // Rotation String "NONE" Rotates the displayed image. One of ["NONE",
    // "QUARTER_CW", "QUARTER_CCW", "HALF"]
    
    public boolean showCrosshair;
    public String crosshairColor;
    public boolean showControls;
    public String rotation;
    
    public CameraWidget(ShuffleboardTab cameraTab)
    {
        this.cameraTab = cameraTab;
    }

    public void setLocation(int row, int column, int height, int width) {
        this.column = column;
        this.row = row;
        this.height = height;
        this.width = width;
        return;
    }
    
    public void name(String name) {
        this.name = name;
        return;
    }

    public void setProperties(boolean showCrosshair, String crosshairColor, boolean showControls, String rotation) {
        this.showCrosshair = showCrosshair;
        this.crosshairColor = crosshairColor;
        this.showControls = showControls;
        this.rotation = rotation;
        return;
    }
    
    public void createCameraShuffleboardWidget(VideoSource camera) {
        // Name Type Default Value Notes
        // ----------------- --------- --------
        // ----------------------------------------------------
        // Show crosshair Boolean true Show or hide a cross-hair on the image
        // Crosshair color Color "white" Can be a string or a rgba integer
        // Show controls Boolean true Show or hide the stream controls
        // Rotation String "NONE" Rotates the displayed image.
        // One of ["NONE", "QUARTER_CW", "QUARTER_CCW", "HALF"]

        Map<String, Object> cameraWidgetProperties = new HashMap<String, Object>();
        cameraWidgetProperties.put("Show crosshair", this.showCrosshair);
        cameraWidgetProperties.put("Crosshair color", this.crosshairColor);
        cameraWidgetProperties.put("Show controls", this.showControls);
        cameraWidgetProperties.put("Rotation", this.rotation);

        cameraTab.add(this.name + " Camera", camera)
                .withWidget(BuiltInWidgets.kCameraStream)
                .withPosition(this.column, this.row)
                .withSize(this.width, this.height)
                .withProperties(cameraWidgetProperties);
    }
    public void createCameraShuffleboardWidgetLL(String camera, String[] URL) {
        // Name Type Default Value Notes
        // ----------------- --------- --------
        // ----------------------------------------------------
        // Show crosshair Boolean true Show or hide a cross-hair on the image
        // Crosshair color Color "white" Can be a string or a rgba integer
        // Show controls Boolean true Show or hide the stream controls
        // Rotation String "NONE" Rotates the displayed image.
        // One of ["NONE", "QUARTER_CW", "QUARTER_CCW", "HALF"]

        Map<String, Object> cameraWidgetProperties = new HashMap<String, Object>();
        cameraWidgetProperties.put("Show crosshair", this.showCrosshair);
        cameraWidgetProperties.put("Crosshair color", this.crosshairColor);
        cameraWidgetProperties.put("Show controls", this.showControls);
        cameraWidgetProperties.put("Rotation", this.rotation);

        // ComplexWidget addCamera(String title, String cameraName, String... cameraUrls)
        cameraTab.addCamera(this.name + " Camera", camera, URL)
                .withWidget(BuiltInWidgets.kCameraStream)
                .withPosition(this.column, this.row)
                .withSize(this.width, this.height)
                .withProperties(cameraWidgetProperties);
    }    
}
