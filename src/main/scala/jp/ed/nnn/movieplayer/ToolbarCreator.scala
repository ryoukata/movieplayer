package jp.ed.nnn.movieplayer

import java.lang

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.{Label, TableView}
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.stage.Stage
import javafx.util.Duration
import jp.ed.nnn.movieplayer.SizeConstants._
import jp.ed.nnn.movieplayer.ToolbarButtonCreator.createButton

object ToolbarCreator {

  def create(mediaView: MediaView, tableView: TableView[Movie], timeLabel: Label, scene: Scene, primaryStage: Stage): HBox = {
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    // first Button
    val firstButton = createButton("images/first.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playPre(tableView, mediaView, timeLabel)
        }
    })

    // back button
    val backButton = createButton("images/back.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000))
          )
        }
    })

    // play button
    val playButton = createButton("images/play.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val selectionMode = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionMode.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    })

    // pause button
    val pauseButton = createButton("images/pause.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) mediaView.getMediaPlayer.pause()
      }
    })

    // forward button
    val forwardButton = createButton("images/forward.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          val player = mediaView.getMediaPlayer
          if (player.getTotalDuration.greaterThan(player.getCurrentTime.add(new Duration(10000)))) {
            player.seek(player.getCurrentTime.add(new Duration(10000)))
          }
        }
    })

    // last Button
    val lastButton = createButton("images/last.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playNext(tableView, mediaView, timeLabel)
        }
    })

    // fullscreen Button
    val fullscreenButton = createButton("images/fullscreen.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        primaryStage.setFullScreen(true)
        mediaView.fitHeightProperty().unbind()
        mediaView.fitHeightProperty().bind(scene.heightProperty())
        mediaView.fitWidthProperty().unbind()
        mediaView.fitWidthProperty().bind(scene.widthProperty())
      }
    })
    primaryStage.fullScreenProperty().addListener(new ChangeListener[lang.Boolean] {
      override def changed(observableValue: ObservableValue[_ <: lang.Boolean], oldValue: lang.Boolean, newValue: lang.Boolean): Unit =
        if (!newValue) {
          mediaView.fitHeightProperty().unbind()
          mediaView.fitWidthProperty().unbind()
          mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
          mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
        }
    })

    toolBar.getChildren.addAll(firstButton, backButton, playButton, pauseButton, forwardButton, lastButton, fullscreenButton, timeLabel)
    toolBar
  }

}
