import React, { useState, useEffect, useMemo, useRef } from "react";
import WalletDataService from "../services/WalletsService";
import { useTable, usePagination, useGlobalFilter, useSortBy } from "react-table";

const WalletsList = (props) => {
  const [Wallets, setWallets] = useState([]);
  const WalletsRef = useRef();
  WalletsRef.current = Wallets;

  useEffect(() => {
    retrieveWallets();
  }, []);

  const retrieveWallets = () => {
    WalletDataService.getAll()
      .then((response) => {
        setWallets(response.data);
      })
      .catch((e) => {
        alert(e.response.data.message);
        console.log(e);
      });
  };

  const openWallet = (rowIndex) => {
    const walletId = WalletsRef.current[rowIndex].walletId;
    props.history.push("/wallets/" + walletId);
  };

  const openWalletDetails = (rowIndex) => {
    const walletId = WalletsRef.current[rowIndex].walletId;
    props.history.push("/walletdetails/" + walletId);
  };

  const openWalletTransfer = (rowIndex) => {
    const walletId = WalletsRef.current[rowIndex].walletId;
    props.history.push("/fundtransfer/" + walletId);
  };


  const columns = useMemo(
    () => [
      {
        Header: "Name",
        accessor: "name",
      },
      {
        Header: "Email",
        accessor: "emailId",
      },
      {
        Header: "Amount",
        accessor: "amount",
      },
      {
        Header: "Actions",
        accessor: "actions",
        Cell: (props) => {
          const rowIdx = props.row.id;
          return (
            <div className="row">
              <div className="col-sm-4">
                <button className="btn btn-outline-info btn-sm" onClick={() => openWallet(rowIdx)}>
            Add/Withdraw <i className="fas fa-wallet"></i>
          </button>
              </div>
              
          <div className="col-sm-4">
              <button className="btn btn-outline-info btn-sm" onClick={() => openWalletDetails(rowIdx)}>
            View Transactions <i className="fas fa-receipt"></i>
          </button>
          </div>

          <div className="col-sm-4">
                <button className="btn btn-outline-info btn-sm" onClick={() => openWalletTransfer(rowIdx)}>
            Fund Transfer <i className="fas fa-exchange-alt"></i>
          </button>
              </div>
            </div>
          );
        },
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
    data: Wallets,
  },
  useGlobalFilter,
  useSortBy,
  usePagination
  );

  return (
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
        <div className="float-right">
        <button className="btn btn-default" onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
          {'First'}
        </button>{' '}
        <button className="btn btn-default" onClick={() => previousPage()} disabled={!canPreviousPage}>
          Previous
        </button>{' '}
        <button className="btn btn-default" onClick={() => nextPage()} disabled={!canNextPage}>
          Next
        </button>{' '}
        <button className="btn btn-default" onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
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
  );
};

export default WalletsList;