package org.stypox.dicio.skills.play_music

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import org.dicio.skill.context.SkillContext
import org.dicio.skill.skill.SkillInfo
import org.dicio.skill.skill.SkillOutput
import org.dicio.skill.standard.StandardRecognizerData
import org.dicio.skill.standard.StandardRecognizerSkill
import org.stypox.dicio.sentences.Sentences.PlayMusic

class PlayMusicSkill(
    correspondingSkillInfo: SkillInfo,
    data: StandardRecognizerData<PlayMusic>,
) : StandardRecognizerSkill<PlayMusic>(correspondingSkillInfo, data) {

    override suspend fun generateOutput(ctx: SkillContext, inputData: PlayMusic): SkillOutput {
        val uriAndMessage = when (inputData) {
            is PlayMusic.Song -> {
                val title = inputData.title?.trim().orEmpty()
                val artist = inputData.artist?.trim().orEmpty()
                if (title.isBlank() || artist.isBlank()) {
                    return PlayMusicOutput("I couldn't identify both the song and its artist.")
                }
                val uri = Uri.Builder()
                    .scheme("parachord")
                    .authority("play")
                    .appendQueryParameter("artist", artist)
                    .appendQueryParameter("title", title)
                    .build()
                Pair(uri, "Playing $title by $artist.")
            }

            is PlayMusic.Album -> {
                val album = inputData.album?.trim().orEmpty()
                val artist = inputData.artist?.trim().orEmpty()
                if (album.isBlank() || artist.isBlank()) {
                    return PlayMusicOutput("I couldn't identify both the album and its artist.")
                }
                val uri = Uri.Builder()
                    .scheme("parachord")
                    .authority("play")
                    .appendPath("album")
                    .appendQueryParameter("artist", artist)
                    .appendQueryParameter("title", album)
                    .build()
                Pair(uri, "Playing the album $album by $artist.")
            }

            is PlayMusic.Artist -> {
                val artist = inputData.artist?.trim().orEmpty()
                if (artist.isBlank()) {
                    return PlayMusicOutput("I couldn't identify the artist.")
                }
                // Parachord's supported artist playback action starts an artist-seeded radio stream.
                val uri = Uri.Builder()
                    .scheme("parachord")
                    .authority("play")
                    .appendPath("radio")
                    .appendQueryParameter("artist", artist)
                    .build()
                Pair(uri, "Starting artist radio for $artist.")
            }

            is PlayMusic.Playlist -> {
                val playlistUrl = inputData.url?.trim().orEmpty()
                val parsed = Uri.parse(playlistUrl)
                val supportedHost = parsed.host in setOf("music.apple.com", "open.spotify.com")
                val isPlaylistPath = parsed.pathSegments.any { it == "playlist" }
                if (!parsed.scheme.equals("https", ignoreCase = true) ||
                    !supportedHost || !isPlaylistPath
                ) {
                    return PlayMusicOutput(
                        "To play a playlist, say 'play playlist link' followed by its Apple Music or Spotify share link. " +
                            "Parachord cannot currently find playlists by name from a voice command."
                    )
                }
                val uri = Uri.Builder()
                    .scheme("parachord")
                    .authority("play")
                    .appendPath("playlist")
                    .appendQueryParameter("url", playlistUrl)
                    .build()
                Pair(uri, "Opening that playlist in Parachord.")
            }
        }

        val intent = Intent(Intent.ACTION_VIEW, uriAndMessage.first)
            .setPackage("com.parachord.android")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return try {
            ctx.android.startActivity(intent)
            PlayMusicOutput(uriAndMessage.second)
        } catch (_: ActivityNotFoundException) {
            PlayMusicOutput("Parachord isn't installed or couldn't open that music request.")
        }
    }
}
