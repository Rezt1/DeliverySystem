import { navigationStuff } from "./src.mjs";

/*const menuButton = document.getElementById("main-menu");
      const mobileMenu = document.getElementById("mobile-menu");

      // Add event listener for toggling the mobile menu
      menuButton.addEventListener("click", function () {
        const isMenuVisible = mobileMenu.classList.contains("hidden");

        // Toggle visibility of the mobile menu
        mobileMenu.classList.toggle("hidden");

        // Update aria-expanded attribute
        menuButton.setAttribute("aria-expanded", !isMenuVisible);
      });

      // Simulating login state (change this to actual login check logic)
      let isLoggedIn = false; // Change to `true` when user logs in
      let isDeliveryGuy = false; // Change to `true` when user is a delivery guy

      // Elements for Login, Register, Account, and Deliveries buttons (Desktop and Mobile)
      const loginButton = document.getElementById("login-button");
      const registerButton = document.getElementById("register-button");
      const accountButton = document.getElementById("account-button");
      const deliveriesButton = document.getElementById("deliveries-button");
      const mobileCartButton = document.getElementById("mobile-cart-button");
      const mobileLoginButton = document.getElementById("mobile-login-button");
      const mobileRegisterButton = document.getElementById(
        "mobile-register-button"
      );
      const mobileAccountButton = document.getElementById(
        "mobile-account-button"
      );
      const mobileDeliveriesButton = document.getElementById(
        "mobile-deliveries-button"
      );

      // Toggle between Login/Register/Account buttons and Deliveries button
      function toggleLoginState() {
        if (isLoggedIn) {
          // When logged in, show Account and hide Login/Register
          loginButton.classList.add("hidden");
          registerButton.classList.add("hidden");
          accountButton.classList.remove("hidden");
          mobileLoginButton.classList.add("hidden");
          mobileRegisterButton.classList.add("hidden");
          mobileAccountButton.classList.remove("hidden");

          // Show or hide Deliveries button based on user type
          if (isDeliveryGuy) {
            deliveriesButton.classList.remove("hidden");
            if (mobileDeliveriesButton)
              mobileDeliveriesButton.classList.remove("hidden");
          } else {
            deliveriesButton.classList.add("hidden");
            if (mobileDeliveriesButton)
              mobileDeliveriesButton.classList.add("hidden");
          }
        } else {
          // When not logged in, show Login/Register and hide Account
          loginButton.classList.remove("hidden");
          registerButton.classList.remove("hidden");
          accountButton.classList.add("hidden");
          mobileLoginButton.classList.remove("hidden");
          mobileRegisterButton.classList.remove("hidden");
          mobileAccountButton.classList.add("hidden");

          // Hide Deliveries button
          deliveriesButton.classList.add("hidden");
          if (mobileDeliveriesButton)
            mobileDeliveriesButton.classList.add("hidden");
        }
      }

      // Call the function to set the initial state (login or account button)
      toggleLoginState(); */
      navigationStuff();
