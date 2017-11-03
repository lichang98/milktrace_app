package com.example.milktracesystem.Camera;


import com.example.milktracesystem.Camera.SourceData;

/**
 * Callback for camera previews.
 */
public interface PreviewCallback {
    void onPreview(SourceData sourceData);
    void onPreviewError(Exception e);
}
