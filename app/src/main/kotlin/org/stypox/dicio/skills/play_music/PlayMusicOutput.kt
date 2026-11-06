package org.stypox.dicio.skills.play_music

import org.dicio.skill.context.SkillContext
import org.stypox.dicio.io.graphical.HeadlineSpeechSkillOutput

class PlayMusicOutput(
    private val message: String,
) : HeadlineSpeechSkillOutput {
    override fun getSpeechOutput(ctx: SkillContext): String = message
}
