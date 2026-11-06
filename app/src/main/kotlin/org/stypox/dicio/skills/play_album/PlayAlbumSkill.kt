package org.stypox.dicio.skills.play_album

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.SkillInfo
import org.dicio.skill.skill.SkillOutput
import org.dicio.skill.standard.StandardRecognizerData
import org.dicio.skill.standard.StandardRecognizerSkill
import org.stypox.dicio.sentences.Sentences.PlayAlbum

class PlayAlbumSkill(
    correspondingSkillInfo: SkillInfo,
    data: StandardRecognizerData<PlayAlbum>,
) : StandardRecognizerSkill<PlayAlbum>(correspondingSkillInfo, data) {

    override suspend fun generateOutput(ctx: SkillContext, inputData: PlayAlbum): SkillOutput {
        val album: String?
        val artist: String?
        when (inputData) {
            is PlayAlbum.Query -> {
                album = inputData.album?.trim()
                artist = inputData.artist?.trim()
            }
        }

        if (album.isNullOrBlank() || artist.isNullOrBlank()) {
            return PlayAlbumOutput(launched = false, album = album, artist = artist)
        }

        // Parachord resolves this URI to the corresponding Apple Music album and starts playback.
        val uri = Uri.Builder()
            .scheme("parachord")
            .authority("play")
            .appendPath("album")
            .appendQueryParameter("artist", artist)
            .appendQueryParameter("title", album)
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri)
            .setPackage("com.parachord.android")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return try {
            ctx.android.startActivity(intent)
            PlayAlbumOutput(launched = true, album = album, artist = artist)
        } catch (_: ActivityNotFoundException) {
            PlayAlbumOutput(launched = false, album = album, artist = artist)
        }
    }
}
