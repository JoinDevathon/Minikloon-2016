package org.devathon.contest2016.mm.mechanics.entities;

import com.google.common.collect.ImmutableMap;
import org.devathon.contest2016.mm.utils.serialization.Codec;

import java.util.HashMap;
import java.util.Map;

public enum MusicEntityType {
    SUPER_NOTE(0, new SuperNote.SuperNoteCodec()),
    MUSIC_STRING(1, new MusicString.MusicStringCodec()),
    SUPER_NOTE_SPAWNER(3, new NoteSpawner.SuperNoteSpawnerCodec())
    ;

    private int id;
    private Codec codec;

    MusicEntityType(int id, Codec codec) {
        this.id = id;
        this.codec = codec;
    }

    public int getId() {
        return id;
    }

    public Codec getCodec() {
        return codec;
    }

    public static final ImmutableMap<Integer, Codec> idsToCodec;
    static {
        Map<Integer, Codec> map = new HashMap<>();
        for(MusicEntityType type : MusicEntityType.values()) {
            map.put(type.getId(), type.getCodec());
        }
        idsToCodec = ImmutableMap.copyOf(map);
    }
}
