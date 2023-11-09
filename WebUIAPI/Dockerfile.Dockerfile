FROM python:3.7.16

WORKDIR /app

#COPY ./app/main.py /app/
#COPY ./app/requirements.txt /app
COPY . /app

RUN pip install --no-cache-dir --upgrade -r /app/requirements.txt
#RUN pip install -r /app/requirements.txt

EXPOSE 8000

CMD [ "python", "main.py" ]
# CMD [ "uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "80" ]