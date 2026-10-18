FROM python:3.12-slim
RUN pip install --no-cache-dir ansible-core==2.17.* docker requests \
 && ansible-galaxy collection install community.docker
WORKDIR /ansible
