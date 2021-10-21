import React from "react";
import { Switch, Route, Link, Redirect } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";
import "./App.css";

import CreateWallet from "./components/CreateWallet";
import Wallet from "./components/Wallet";
import WalletsList from "./components/WalletsList";
import WalletDetails from "./components/WalletDetails";
import WalletFundTransfer from "./components/WalletFundTransfer";


function App() {
  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <a href="/wallets" className="navbar-brand">
          K+N Wallets
        </a>
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={"/wallets"} className="nav-link">
              Wallets
            </Link>
          </li>
          <li className="nav-item">
            <Link to={"wallets/create"} className="nav-link">
              Create Wallet
            </Link>
          </li>
        </div>
      </nav>

      <div className="container mt-3">
        <Switch>
          <Route exact path={["/wallets"]} component={WalletsList} />
          <Route exact path="/wallets/create" component={CreateWallet} />
          <Route exact path="/wallets/:id" component={Wallet} />
          <Route exact path="/wallets/:id/details" component={WalletDetails} />
          <Route exact path="/wallets/:id/transfer" component={WalletFundTransfer} />
          <Redirect from="/" to="/wallets" />
        </Switch>
      </div>
    </div>
  );
}

export default App;
