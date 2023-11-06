FROM python:3.7.16

WORKDIR /app

COPY ./app /app/
COPY ./requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /app/requirements.txt

EXPOSE 80

CMD [ "python", "main.py" ]
# CMD [ "uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "80" ]