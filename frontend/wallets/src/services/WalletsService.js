import http from "../http-common";

const getAll = () => {
  return http.get("/wallets");
};

const get = (walletId) => {
  return http.get(`/wallets/${walletId}`);
};

const create = (data) => {
  return http.post("/wallets", data);
};

const update = (walletId, data) => {
  return http.post(`/wallets/${walletId}`, data);
};

const transferFunds = (walletId, data, targetWalletId) => {
  return http.post(`/wallets/${walletId}/transfer/${targetWalletId}`, data);
};

const WalletsService = {
  getAll,
  get,
  create,
  update,
  transferFunds,
};

export default WalletsService;
