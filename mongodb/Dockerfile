FROM frodenas/mongodb

# dumb-init
RUN wget -O /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.1.3/dumb-init_1.1.3_amd64
RUN chmod +x /usr/local/bin/dumb-init

ENV MONGODB_USERNAME ekanban
ENV MONGODB_PASSWORD nabnake
ENV MONGODB_DBNAME ekanban

ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
CMD ["/scripts/run.sh"]
