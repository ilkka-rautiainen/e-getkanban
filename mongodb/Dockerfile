FROM mongo:3.3

RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates wget netcat

# dumb-init
RUN wget -O /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.1.3/dumb-init_1.1.3_amd64
RUN chmod +x /usr/local/bin/dumb-init

COPY ./run.sh /scripts/run.sh

ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
CMD ["/scripts/run.sh"]
