
import os
import json
from json import JSONEncoder
import sys
import numpy as np
import pandas as pd
import requests as rqt
import joblib
from sklearn.preprocessing import MinMaxScaler
from ast import literal_eval

#Chargement des donnees depuis PHP:
data = json.loads(sys.argv[1])
df=pd.DataFrame(data)
#Normalization:
x_scaler_filename = "x_scaler.save"
y_scaler_filename = "y_scaler.save"
scaler_x = joblib.load(x_scaler_filename)
scaler_y = joblib.load(y_scaler_filename)
t = df.values
norm_data = scaler_x.transform(t)

#envoi des donnees vers API:
req_content={'data': str(norm_data.tolist())}
encoded_data = json.dumps(req_content)
url_api='https://6rdgrzhr4a.execute-api.us-east-1.amazonaws.com/finalparkin/parkin'
#Recuperation des donnees:
result_api=rqt.post(url_api,data=encoded_data)
result=result_api.content
#Denormalization:
np_result=np.array(literal_eval(result))
np_result = np_result.reshape((np_result.size, 1))
final_result = scaler_y.inverse_transform(np_result).astype(int)
#transformation en json et renvoi au PHP:

a=df[[0,1]].values
ar1=final_result.tolist()
ar2=a.tolist()
final_vec=df[[0,1,'resultat']].values
dff=pd.DataFrame(final_vec, columns=["horodateur_latitude", "horodateur_longitude","horodateur_nb_places_reel"])
json_format=dff.to_json(orient="records")
print(json_format)


