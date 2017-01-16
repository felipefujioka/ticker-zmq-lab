docker stop device-container
docker rm device-container

docker build -t device-image .

docker run -p 3001:3001 --name device-container device-image