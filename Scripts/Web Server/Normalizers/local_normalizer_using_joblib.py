
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from sklearn.externals import joblib
import numpy as np

y = pd.read_csv("https://parkin-projet-annuel-project.s3.amazonaws.com/y_normalize.csv",sep=';')
x = pd.read_csv("https://parkin-projet-annuel-project.s3.amazonaws.com/x_normalize.csv",sep=';')
y=y.astype(int)
x=x.astype(float)
y = np.reshape(y, (-1,1))
scaler_x = MinMaxScaler()
scaler_y = MinMaxScaler()
scaler_x.fit(x)
scaler_y.fit(y)
y_scaler_filename = "y_scaler.save"
x_scaler_filename = "x_scaler.save"

joblib.dump(scaler_x, x_scaler_filename)
joblib.dump(scaler_y, y_scaler_filename)

