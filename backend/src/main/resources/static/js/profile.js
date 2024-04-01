function createThreadHtml(thread) {
  return `<a
    href="/t/${thread.name}"
    class="list-group-item list-group-item-action d-flex justify-content-between align-items-center"
  >
  ${thread.name}
    <span class="badge badge-primary badge-pill"
      >${thread.posts.length} posts</span
    >
  </a>`;
}

function getThreads() {
  const url =
    "https://10.100.139.188:8443/thread/user/paginated?page=" +
    page +
    "&size=10" +
    "&username=" +
    username;
  addNewElements(createThreadHtml, url, "loadMoreButton", "thread-container");
}

function getMoreElements() {
  getThreads();
}
