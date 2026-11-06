package org.stypox.dicio.skills.play_album

import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.SkillOutput
import org.stypox.dicio.R
import org.stypox.dicio.io.graphical.HeadlineSpeechSkillOutput
import org.stypox.dicio.util.getString

class PlayAlbumOutput(
    private val launched: Boolean,
    private val album: String?,
    private val artist: String?,
) : HeadlineSpeechSkillOutput {
    override fun getSpeechOutput(ctx: SkillContext): String = if (launched) {
        ctx.getString(R.string.skill_play_album_started, album, artist)
    } else {
        ctx.getString(R.string.skill_play_album_unavailable)
    }
}
