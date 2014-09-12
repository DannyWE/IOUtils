package spike

class UserModule {

  type User <: UserLike

  trait UserLike { this: User =>
    def id: String
    def name: String

  }

}
