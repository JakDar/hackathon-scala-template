up:
	docker-compose -f docker_services.yaml up -d

down:
	docker-compose -f docker_services.yaml down

restart:
	docker-compose -f docker_services.yaml down && docker-compose -f docker_services.yaml up
