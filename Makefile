run:
	./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DJWT_SECRET=$(JWT_SECRET) -DRESEND_API_KEY=$(RESEND_API_KEY)"
