import React, { useState, useEffect, useRef } from "react";
import WalletDataService from "../services/WalletsService";

const WalletFundTransfer = props => {
  const initialWalletState = {
    walletId: null,
    name: "",
    email: "",
    amount: "",
    transferAmount: 0,
    targetWalletId:""
  };
  const [currentWallet, setCurrentWallet] = useState(initialWalletState);
  const [Emails, setEmails] = useState([]);
  const EmailsRef = useRef();
  EmailsRef.current = Emails;

    useEffect(() => {
    getWallet(props.match.params.id);
  }, [props.match.params.id]);

  const getWallet = id => {
    WalletDataService.get(id)
      .then(response => {
        setCurrentWallet(response.data);
        console.log(response.data);
      })
      .catch(e => {
        alert(e.response.data.message);
        console.log(e);
      });
  };

  const retrieveWallets = () => {
    const emails=[];
    WalletDataService.getAll()
      .then((response) => {
        for (let i = 0; i < response.data.length; i++) {
          var data = {
            targetWalletId: response.data[i].walletId,
            targetEmailId: response.data[i].emailId,
            targetWalletName: response.data[i].name
          }
          emails.push(data);
        }
        setEmails(emails);       
      })
      .catch((e) => {
        alert(e.response.data.message);
        console.log(e);
      });
  };

  const handleInputChange = event => {
    const { name, value } = event.target;
    setCurrentWallet({ ...currentWallet, [name]: value });
  };

  const transferWallet = (walletId) => {
    if (currentWallet.targetWalletId === undefined || currentWallet.targetWalletId === ''){
      alert("Please select Wallet Transfer To User");
      return false;
    }

    if(walletId == currentWallet.targetWalletId){
      alert("Same Wallet Transfer Not Allowed");
      return false;
    }

    if (currentWallet.transferAmount === undefined || currentWallet.transferAmount === ''){
      alert("Transfer Amount cannot be empty / Zero / Format Not allowed");
      return false;
    }
    var localTransferAmount = parseInt(currentWallet.transferAmount);
    if(localTransferAmount === 0){
      alert("Transfer Amount cannot be Zero");
      return false;
    }
    if(localTransferAmount < 0){
      alert("Negative Amount Not Allowed");
      return false;
    }

    const transaction= {
      name: "Fund Transfer",
      transactionAmount: localTransferAmount
    }
    WalletDataService.transferFunds(walletId, transaction, currentWallet.targetWalletId)
      .then(response => {
        console.log(response.data);
        alert("The Fund Transfer successful!");
        props.history.push("/wallets");
      })
      .catch(e => {
        alert(e.response.data.message);
        console.log(e);
      });
  };

  const cancelRequest = () => {
        props.history.push("/wallets");
  };

  return (
    <div>
        <div className="edit-form">
          <h4>Wallet Fund Transfer</h4>
          <form>
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                className="form-control"
                id="name"
                name="name"
                value={currentWallet.name}
                disabled
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="emailId">EmailId</label>
              <input
                type="text"
                className="form-control"
                id="emailId"
                name="emailId"
                disabled
                value={currentWallet.emailId}
                onChange={handleInputChange}
              />
            </div>

             <div className="form-group">
              <label htmlFor="amount">Available Amount</label>
              <input
                type="number"
                className="form-control"
                id="amount"
                name="amount"
                disabled
                value={currentWallet.amount}
                onChange={handleInputChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="targetWalletId">Select Wallet Transfer To User</label>
              <select className="form-control" name="targetWalletId" id="targetWalletId" onChange={handleInputChange} onClick={retrieveWallets}>
                <option disabled selected value> -- select an option -- </option>
                {Emails.map((targetObject) => (
              <option key={targetObject.targetWalletId} value={targetObject.targetWalletId}>{targetObject.targetWalletName} ( {targetObject.targetEmailId} )</option>
            ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="transferAmount">Transfer Amount</label>
              <input
                type="number"
                min="0"
                className="form-control"
                id="transferAmount"
                name="transferAmount"
                value={currentWallet.transferAmount}
                onChange={handleInputChange}
              />
            </div>
          </form>

          <div className="row">
          <div className="col-md-4">
           <button className="btn btn-outline-success btn-sm" onClick={() => transferWallet(currentWallet.walletId)}>
            Transfer Fund <br></br><i className="fas fa-exchange-alt"></i>
          </button>
          </div>

          <div className="col-md-4"></div>

          <div className="col-sm-4">
           <button className="btn btn-outline-info btn-sm" onClick={cancelRequest}>
            Back to Wallets 
            <br></br>
            <i className="fas fa-arrow-left"></i>
          </button>
          </div>

          </div>
        </div>
    </div>
  );
};

export default WalletFundTransfer;
