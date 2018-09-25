import os
from flask import Flask, render_template, flash, request, redirect, url_for, send_from_directory
from werkzeug.utils import secure_filename
from flask_uploads import UploadSet, configure_uploads, ALL
from sqlalchemy import create_engine, Column, Integer, String, MetaData
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from flask_marshmallow import Marshmallow

UPLOAD_FOLDER = 'uploads'
app = Flask(__name__)
app.config['UPLOADED_FILES_DEST'] = UPLOAD_FOLDER

engine = create_engine('mysql://root:bluebird@localhost/audio')
metadata = MetaData()
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()
con = engine.connect()
ma = Marshmallow(app)

def init_db():
    metadata.create_all(bind=engine)

class Records():
    __tablename__ = "records"
    Sr_no = Column(Integer, primary_key=True)
    word = Column(String(80), unique=True, nullable=False)
    location = Column(String(120), unique=True, nullable=False)

class AudioSchema(ma.Schema):
    class Meta:
        # Fields to expose
        fields = ('Sr_no','word', 'location')

audio_schema = AudioSchema()
audio_schema = AudioSchema(many=True)
@app.route("/", methods=['GET', 'POST'])
def main():
    
    return render_template('index.html')

@app.route("/about")
def about():
    data = con.execute("SELECT * FROM records")
    return render_template('entries.html', data = data)

files = UploadSet('files',ALL)
configure_uploads(app,files)
@app.route("/uploads", methods=['GET', 'POST'])
def uploader():
    if request.method == 'POST':
        userDetails = request.form
        word = userDetails['word']
        filename = '/uploads/'+files.save(request.files['file'],name=word)
        con.execute("UPDATE records set location = %s where word = %s",[filename,word])
        return render_template('index.html')

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)