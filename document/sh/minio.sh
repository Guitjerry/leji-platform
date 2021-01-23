docker run -p 9000:9000 --name minio \
-d --restart=always \
-e "MINIO_ACCESS_KEY=admin" \
-e "MINIO_SECRET_KEY=admin123456" \
-v /home/data:/data \
-v /home/config:/root/.minio \
minio/minio server /usr/local/minioFile
