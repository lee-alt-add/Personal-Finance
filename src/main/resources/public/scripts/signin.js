const signInContainer = document.getElementById('sign-in');
const signUpContainer = document.getElementById('sign-up');
const signInBtn = document.getElementById('sign-in-button');
const signInForm = document.getElementById("signinForm");
const signUpLink = document.getElementById('sign-up-link');
let signInLink = document.getElementById('sign-in-link');

signUpLink.addEventListener("click", () => {
    signUpContainer.style.display = 'none';
    signInContainer.style.display = 'block';
    signInBtn.textContent = 'Sign up';
});

signInLink.addEventListener("click", () => {
    signUpContainer.style.display = 'block';
    signInContainer.style.display = 'none';
    signInBtn.textContent = 'Sign in';
});

signInForm.addEventListener("submit", async function(e) {
    e.preventDefault();

    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const link = signInBtn.textContent === 'Sign in' ? "/users/signin" : "/users";

    const response = await fetch(link, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user)
    });

    const savedUser = await response.json();

    if (savedUser && savedUser.id) {
        window.location.href = `/dashboard.html?id=${savedUser.id}`;
    } else {
        alert("Sign in failed");
    }
});

