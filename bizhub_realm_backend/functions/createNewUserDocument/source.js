exports = async function createNewUserDocument({user}) {
  
  const cluster = context.services.get("mongodb-atlas");
  const users = cluster.db("tracker").collection("User");
  return users.insertOne({
    _id: user.id,
    _partition: `user=${user.id}`,
    name: user.data.email,
    canReadPartitions: [`user=${user.id}`],
    canWritePartitions: [`project=${user.id}`],
    memberOf: [
      {"name": "Example Project", "partition": `project=${user.id}`, "description": "This is an example project. Swipe to delete | Click to see Tasks | Menu to add Members",  "created": "Today"}
    ],
  });
};
