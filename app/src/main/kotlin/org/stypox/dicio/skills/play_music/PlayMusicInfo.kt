package org.stypox.dicio.skills.play_music

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.Skill
import org.dicio.skill.skill.SkillInfo
import org.stypox.dicio.sentences.Sentences

object PlayMusicInfo : SkillInfo("play_music") {
    override fun name(context: Context) = "Parachord music playback"

    override fun sentenceExample(context: Context) =
        "Play the album Abbey Road by The Beatles"

    @Composable
    override fun icon() =
        rememberVectorPainter(Icons.AutoMirrored.Filled.QueueMusic)

    override fun build(ctx: SkillContext): Skill<*>? {
        val data = Sentences.PlayMusic[ctx.sentencesLanguage] ?: return null
        return PlayMusicSkill(PlayMusicInfo, data)
    }
}
