mongo ${MONGO_DATABASE} --eval "
    db.initial.insert({'info':'build_success'});
    db.createUser({ user: '${MONGO_USER}', pwd: '${MONGO_PASSWORD}', roles: [{ role: 'readWrite', db: '${MONGO_DATABASE}' }] });
"