import cv2 
from flask import Flask, render_template, Response
from emotion_detect import EmotionDetect


app = Flask(__name__)

@app.route('/')
def video_feed():
    """Getting the output of the Emotion_detect on the flask server in order to request it with furhat."""
    return EmotionDetect()

app.run()