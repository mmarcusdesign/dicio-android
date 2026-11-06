package org.stypox.dicio.skills.play_music

import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.SkillOutput
import org.dicio.skill.skill.HeadlineSpeechSkillOutput

class PlayMusicOutput(
    private val message: String,
) : HeadlineSpeechSkillOutput {
    override fun getSpeechOutput(ctx: SkillContext): String = message
}
