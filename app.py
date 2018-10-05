import os
import json
from flask import Flask, render_template, flash, request, redirect, url_for, send_from_directory,jsonify
from werkzeug.utils import secure_filename
from flask_uploads import UploadSet, configure_uploads, ALL
from sqlalchemy import create_engine, Column, Integer, String, MetaData
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from flask_sqlalchemy import SQLAlchemy
UPLOAD_FOLDER = 'uploads'
app = Flask(__name__)
app.config['UPLOADED_FILES_DEST'] = UPLOAD_FOLDER
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:bluebird@localhost/audio'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
engine = create_engine('mysql://root:bluebird@localhost/audio')
metadata = MetaData()
db = SQLAlchemy(app)
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()
db.create_all()
con = engine.connect()

def init_db():
    metadata.create_all(bind=engine)

class Records(db.Model):
    __tablename__ = "records"
    word = Column(String(80), primary_key=True)
    location = Column(String(120))
    def __init__(self, word,location):
        self.word = word
        self.location = location
    @property
    def serialize(self):
       """Return object data in easily serializeable format"""
       return {
           'word' : self.word,
           'location' : self.location      
       }
    def __repr__(self):
        return "<Word: {}\n Location: {}".format(self.word, self.location)

@app.route("/createdb", methods=['GET'])
def database_gen():
    db.create_all()
    obj = Records(word = "hello", location="/uploads")
    db.session.add(obj)
    db.session.commit()
    return "Executed successfully"

@app.route("/", methods=['GET'])
def main():
    return render_template('index.html')

@app.route("/about", methods=['POST'])
def about():
    query_result = Records.query.filter_by(location="/uploads").all()
    return jsonify([i.serialize for i in query_result])
    # records_dict = {}
    # records_list = []
    # for record in none_records:
    #     records_dict.update({'word': record.word, 'location': record.location})
    #     records_list.append(records_dict)
    # return jsonify(records_list)

files = UploadSet('files',ALL)
configure_uploads(app,files)

@app.route("/uploads", methods=['POST'])
def uploader():
    word="hello"
    uploaded_file = request.files['file']     
    filename = '/uploads/'+files.save(uploaded_file,name=word)
    # con.execute("UPDATE records set location = %s where word = %s",[filename,word])
    update_location = Records.query.filter_by(word=word)
    update_location.location = filename
    db.session.add(update_location)
    db.session.commit()
    return render_template('index.html')

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)