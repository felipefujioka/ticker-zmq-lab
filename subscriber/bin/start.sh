docker stop broadcaster-container
docker rm broadcaster-container

docker build -t broadcaster-image .

docker run --name broadcaster-container broadcaster-image