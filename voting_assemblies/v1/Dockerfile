FROM ubuntu:18.04

ADD file:f278386b0cef68136129f5f58c52445590a417b624d62bca158d4dc926c340df in /

 CMD ["/bin/sh"]
/bin/sh -c apk add --no-cache java-cacerts
 ENV JAVA_HOME=/opt/openjdk-17
 ENV PATH=/opt/openjdk-17/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
 ENV JAVA_VERSION=17-ea+14
/bin/sh -c set -eux; 		arch="$(apk --print-arch)"; 	case "$arch" in 		'x86_64') 			downloadUrl='https://download.java.net/java/early_access/alpine/14/binaries/openjdk-17-ea+14_linux-x64-musl_bin.tar.gz'; 			downloadSha256='f07a1ac921333dafac1cd886ad49600ce143be7efebd32e1a02599a8a0829dd4'; 			;; 		*) echo >&2 "error: unsupported architecture: '$arch'"; exit 1 ;; 	esac; 		wget -O openjdk.tgz "$downloadUrl"; 	echo "$downloadSha256 *openjdk.tgz" | sha256sum -c -; 		mkdir -p "$JAVA_HOME"; 	tar --extract 		--file openjdk.tgz 		--directory "$JAVA_HOME" 		--strip-components 1 		--no-same-owner 	; 	rm openjdk.tgz*; 		rm -rf "$JAVA_HOME/lib/security/cacerts"; 	ln -sT /etc/ssl/certs/java/cacerts "$JAVA_HOME/lib/security/cacerts"; 		java -Xshare:dump; 		fileEncoding="$(echo 'System.out.println(System.getProperty("file.encoding"))' | jshell -s -)"; [ "$fileEncoding" = 'UTF-8' ]; rm -rf ~/.java; 	javac --version; 	java --version
 CMD ["jshell"]
 ENV TZ:=America/Sao_Paulo
 VOLUME [/tmp]
COPY file:00dad86aea10177c7f84ad96f17ecff327c1b263ef38f52d096fc70fde49a709 in VotingAssemblies.jar
 ENTRYPOINT ["java" "-jar" "VotingAssemblies.jar"]
