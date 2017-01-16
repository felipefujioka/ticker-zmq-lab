docker stop broadcaster-container
docker rm broadcaster-container

docker build -t broadcaster-image .

docker run -p 3002:3002 --name broadcaster-container broadcaster-image