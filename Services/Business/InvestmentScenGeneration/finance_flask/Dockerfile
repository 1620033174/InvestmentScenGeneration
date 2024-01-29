FROM python:3.8

RUN mkdir -p /home/app
WORKDIR /home/app
ADD ./requirements.txt /home/app/requirements.txt 

RUN pip3 install -i https://pypi.tuna.tsinghua.edu.cn/simple --upgrade pip 
RUN pip3 install -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt


EXPOSE 5000
ADD . /home/app
# CMD python ./app/main.py
# CMD ["sh", "-c", "export FLASK_APP=./app/main.py && flask run --host=0.0.0.0"]

CMD ["sh", "-c", "export FLASK_APP=./app/main.py && flask run --host=0.0.0.0"]