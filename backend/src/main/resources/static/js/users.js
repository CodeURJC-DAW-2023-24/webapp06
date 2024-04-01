var page = 0;
var isSearchMode = false;
var lastSearchQuery = "";

document.addEventListener("DOMContentLoaded", function () {
  loadUsers();
});

function createUserHtml(user) {
  return `
    <div class="list-group-item d-flex align-items-center justify-content-between">
      <div class="d-flex align-items-center">
        <img src="/image/user/${user.username}" class="rounded-circle" style="width: 50px; height: 50px" alt="User Photo" />
        <strong class="ml-3">${user.username}</strong>
      </div>
      <div>
        <div>
          <a class="btn btn-link text-primary text-decoration-none p-0 mr-2" title="view profile" href="/user/profile/${user.username}">
            <i class="fas fa-eye"></i>
          </a>
          <a class="btn btn-link text-primary text-decoration-none p-0 mr-2" title="edit profile" href="/user/edit-profile?username=${user.username}">
            <i class="fas fa-edit"></i>
          </a>
          <button type="button" class="btn btn-link text-decoration-none btn-delete p-0 mr-2" data-toggle="modal" data-target="#deleteAccountModal${user.username}">
            <i class="fas fa-trash-alt btn-delete"></i>
          </button>
        </div>
      </div>
    </div>

    <div class="modal fade" id="deleteAccountModal${user.username}" tabindex="-1" aria-labelledby="modalLabel${user.username}" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="modalLabel${user.username}">Confirm Deletion</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            Are you sure you want to delete your account? This action cannot be undone.
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
            <a type="button" class="btn btn-danger btn-confirm-delete" href="/user/delete/${user.username}">Delete Account</a>
          </div>
        </div>
      </div>
    </div>
  `;
}

async function loadUsers() {
  isSearchMode = false;
  lastSearchQuery = "";
  const url =
    "https://10.100.139.188:8443/user/paginated?page=" + page + "&size=10";

  addNewElements(createUserHtml, url, "loadMoreButton", "user-container");
}

async function searchUser(lastSearch, pageParameter = 0) {
  page = pageParameter;
  var username;
  isSearchMode = true;

  if (lastSearch != null) {
    username = lastSearch;
  } else {
    username = document.getElementById("usernameInput").value;
    const container = document.getElementById("user-container");
    container.innerHTML = "";
  }

  const url =
    "https://10.100.139.188:8443/user/search?page=" +
    page +
    "&size=10&username=" +
    username;

  addNewElements(createUserHtml, url, "loadMoreButton", "user-container");
}

function getMoreElements() {
  if (isSearchMode) {
    searchUser(lastSearchQuery, page);
  } else {
    loadUsers();
  }
}
