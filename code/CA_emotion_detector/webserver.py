# -*- coding: utf-8 -*-
"""
Created on Thu Nov 14 18:57:44 2019
@author: seraj
"""

import cv2 
from flask import Flask, render_template, Response
from emotion_detect import EmotionDetect


app = Flask(__name__)

@app.route('/')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return EmotionDetect()

app.run()