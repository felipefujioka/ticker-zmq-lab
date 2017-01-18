docker stop ticker-container
docker rm ticker-container

docker build -t ticker-image .

docker run -p 3000:3000 --name ticker-container ticker-image