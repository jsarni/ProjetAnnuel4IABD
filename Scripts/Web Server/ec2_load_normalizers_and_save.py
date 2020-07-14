import urllib.request

myurl = "https://parkin-projet-annuel-project.s3.amazonaws.com/y_scaler.save"

with urllib.request.urlopen (myurl) as url:
        s=url.read()
        with open("y_scaler.save",'wb')as f:
                f.write(s)

myurl1 = "https://parkin-projet-annuel-project.s3.amazonaws.com/x_scaler.save"


with urllib.request.urlopen (myurl1) as url1:
        s=url1.read()
        with open("x_scaler.save",'wb')as f:
                f.write(s)

