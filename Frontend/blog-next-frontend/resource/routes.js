const Routes = {
  posts: {
    index: "/posts",
    make: "/posts/makePost",
    edit: (post) => "/posts/editPost",
    post: (postID) => `/posts/${postID}`,
  },

  users: {
    index: "/profile",
    login: "/profile/login",
    register: "/profile/register",
    settings: "/profile/settings",
    specific: (username) => `/profile/${username}`,
  },

  admin: {
    index: "/admin",
    page: "/pageSettings",
    post: "/postSettings",
    users: "/userHandling",
    super: "/superController",
  }
};

export default Routes;
