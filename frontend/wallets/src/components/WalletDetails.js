import React, { useState, useEffect, useMemo } from "react";
import WalletDataService from "../services/WalletsService";
import { useTable, usePagination, useGlobalFilter, useSortBy } from "react-table";

const Wallet = walletDetails => {
  const initialWalletState = {
    walletId: null,
    name: "",
    email: "",
    amount: "",
    transactions:[]
  };
  const [currentWallet, setCurrentWallet] = useState(initialWalletState);

  const getWallet = walletId => {
    const idRegex = /^\d+$/;
    if(idRegex.test(walletId)){
    WalletDataService.get(walletId)
      .then(response => {
        setCurrentWallet(response.data);
        console.log(response.data);
      })
      .catch(e => {
        alert("Wallet id Not Found");
        walletDetails.history.push("/wallets");
        console.log(e);
      });
    }else{
      alert("Invalid Request");
      walletDetails.history.push("/wallets");
    }
  };

  useEffect(() => {
    getWallet(walletDetails.match.params.id);
  }, [walletDetails.match.params.id]);

  const handleInputChange = event => {
    const { name, value } = event.target;
    setCurrentWallet({ ...currentWallet, [name]: value });
  };


  const cancelRequest = () => {
        walletDetails.history.push("/wallets");
  };

  const openWallet = (walletId) => {
    walletDetails.history.push("/wallets/" + walletId);
  };

  const openWalletTransfer = (walletId) => {
    walletDetails.history.push("/wallets/"+walletId+"/transfer");
  };

  const columns = useMemo(
    () => [
      {
        Header: "Message",
        accessor: "message",
      },
      {
        Header: "type",
        accessor: "type",
      },
      {
        Header: "Amount",
        accessor: "transactionAmount",
      },
      {
        Header: "TransactionReference",
        accessor: "transactionReferenceId",
      },
      {
        Header: "Created",
        accessor: "created",
      },
      {
        Header: "Created By",
        accessor: "createdBy",
      },
    ],
    []
  );

    const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    prepareRow,
    setGlobalFilter,
    page,
    canPreviousPage,
    canNextPage,
    pageOptions,
    pageCount,
    gotoPage,
    nextPage,
    previousPage,
    setPageSize,
    state: { pageIndex, pageSize, globalFilter }
  } = useTable({
    columns,
    data: currentWallet.transactions,
  },
  useGlobalFilter,
  useSortBy,
  usePagination
  );

  return (
    <div class="row">
      <div class="float-left col-sm-3">
        <div className="edit-form">
          <h4>Wallet Details</h4>
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
              <label htmlFor="emailId">Email Id</label>
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
          </form>

          <div class="row">
          <div class="col-sm-4">
           <button class="btn btn-outline-info btn-sm" onClick={() => openWallet(currentWallet.walletId)}>
            Add / Withdraw <i class="fas fa-wallet"></i>
          </button>
          </div>

          <div class="col-sm-4">
                <button class="btn btn-outline-info btn-sm" onClick={() => openWalletTransfer(currentWallet.walletId)}>
            Fund Transfer <i class="fas fa-exchange-alt"></i>
          </button>
              </div>

          <div class="col-sm-4">
           <button class="btn btn-outline-info btn-sm" onClick={cancelRequest}>
            Back to Wallets 
            <br></br>
            <i class="fas fa-arrow-left"></i>
          </button>
          </div>

          </div>
        </div>
    </div>
    <div class="float-right col-sm-9">
      <h4>Transaction Details</h4>
      
    <div className="list row">
      <div className="col-md-12">
        <div className="input-group mb-3">
          <input
           type="text"
            className="form-control"
            placeholder="Search By Any Field"
           value={globalFilter || ""}
            onChange={e => setGlobalFilter(e.target.value)}
         />
        </div>
      </div>
      <div className="col-md-12 list">
        <table
          className="table table-striped table-bordered"
          {...getTableProps()}
        >
          <thead>
            {headerGroups.map((headerGroup) => (
              <tr {...headerGroup.getHeaderGroupProps()}>
                {headerGroup.headers.map((column) => (
                  <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                    {column.render("Header")}
                </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody {...getTableBodyProps()}>
            {page.map((row) => {
              prepareRow(row);
              return (
                <tr {...row.getRowProps()}>
                  {row.cells.map((cell) => {
                    return (
                      <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                    );
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>
        <div class="float-right">
        <button class="btn btn-default" onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
          {'First'}
        </button>{' '}
        <button class="btn btn-default" onClick={() => previousPage()} disabled={!canPreviousPage}>
          Previous
        </button>{' '}
        <button class="btn btn-default" onClick={() => nextPage()} disabled={!canNextPage}>
          Next
        </button>{' '}
        <button class="btn btn-default" onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
          {'Last'}
        </button>{' '}
        <span>
          Page{' '}
          <strong>
            {pageIndex + 1} of {pageOptions.length}
          </strong>{' '}
        </span>
        <span>
          | Go to page:{' '}
          <input
            type='number'
            defaultValue={pageIndex + 1}
            onChange={e => {
              const pageNumber = e.target.value ? Number(e.target.value) - 1 : 0
              gotoPage(pageNumber)
            }}
            style={{ width: '50px' }}
          />
        </span>{' '}
        <select
          value={pageSize}
          onChange={e => setPageSize(Number(e.target.value))}>
          {[5, 10, 20, 50].map(pageSize => (
            <option key={pageSize} value={pageSize}>
              Show {pageSize}
            </option>
          ))}
        </select>
      </div>
      </div>
    </div>
  
    </div>
    </div>
  );
};

export default Wallet;
