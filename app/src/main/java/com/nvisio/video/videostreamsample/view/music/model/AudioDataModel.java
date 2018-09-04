
package com.nvisio.video.videostreamsample.view.music.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioDataModel {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("audio")
    @Expose
    private List<Audio> audio = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Audio> getAudio() {
        return audio;
    }

    public void setAudio(List<Audio> audio) {
        this.audio = audio;
    }

}
