package spike

class MongoUserModule extends UserModule {

  class User extends UserLike {
    override def id: String = ???

    override def name: String = ???
  }

}
