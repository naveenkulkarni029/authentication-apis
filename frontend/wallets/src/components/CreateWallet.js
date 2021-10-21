import React, { useState } from "react";
import WalletsDataService from "../services/WalletsService";

const CreateWallet = createWallet => {
  const initialWalletState = {
    walletId: null,
    name: "",
    emailId: "",
    amount: "",
  };


  const [Wallet, setWallet] = useState(initialWalletState);

  const handleInputChange = event => {
    const { name, value } = event.target;
    setWallet({ ...Wallet, [name]: value });
  };

  const saveWallet = () => {
    const nameRegex= /^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$/;
    if(!Wallet.name || nameRegex.test(Wallet.name) === false){
      alert("Invalid Name Format");
    return false;
    }
    const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if(!Wallet.emailId || emailRegex.test(Wallet.emailId) === false){
      alert("Invalid Email id Format");
    return false;
    }

    const idRegex = /^\d+$/;
    if(!idRegex.test(Wallet.amount)){
    alert("Invalid Amount format, allowed positive number without decimal only");
    return false;
    }

    if (Wallet.amount === undefined || Wallet.amount === ''){
      alert("Amount cannot be empty / Format Not allowed");
      return false;
    }
    var localwalletAmount = parseInt(Wallet.amount);
    if(localwalletAmount < 0){
      alert("Negative Amount Not Allowed");
      return false;
    }

    var data = {
      name: Wallet.name,
      emailId: Wallet.emailId,
      amount: Wallet.amount
    };

    WalletsDataService.create(data)
      .then(response => {
        alert("Wallet created successfully");
        createWallet.history.push("/wallets");
        console.log(response.data);
      })
      .catch(e => {
        alert(e.response.data.message);
        console.log(e);
      });
  };

  const newWallet = () => {
    setWallet(initialWalletState);
  };

  const cancelRequest = () => {
        createWallet.history.push("/wallets");
  };

  return (
    <div className="submit-form">
      <h4>Create Wallet</h4>
        <div>
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              className="form-control"
              id="name"
              required
              value={Wallet.name}
              onChange={handleInputChange}
              name="name"
            />
          </div>

          <div className="form-group">
            <label htmlFor="emailId">Email Id</label>
            <input
              type="text"
              className="form-control"
              id="emailId"
              required
              value={Wallet.emailId}
              onChange={handleInputChange}
              name="emailId"
            />
          </div>

          <div className="form-group">
            <label htmlFor="amount">Add Amount</label>
            <input
              type="number"
              min="0"
              className="form-control"
              id="amount"
              required
              value={Wallet.amount}
              onChange={handleInputChange}
              name="amount"
            />
          </div>

<         div className="row">
  <div className="col-sm-4">
          <button onClick={saveWallet} className="btn btn-outline-success btn-sm">
            Create Wallet <br></br><i className="far fa-plus-square"></i>
          </button>
          </div>
<div className="col-sm-4">
          <button onClick={newWallet} className="btn btn-outline-secondary btn-sm">
            Clear Details <br></br><i className="fas fa-hands-wash"></i>
          </button>
          </div>
<div className="col-sm-4">
           <button className="btn btn-outline-info btn-sm" onClick={cancelRequest}>
            Back 
            to
             Wallets 
             <br></br>
            <i className="fas fa-arrow-left"></i>
            
          </button>
          </div>
</div>
          
        </div>
    </div>
  );
};

export default CreateWallet;
