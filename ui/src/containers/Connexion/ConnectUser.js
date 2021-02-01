import React from "react";
import {useHistory} from "react-router-dom";
import Connexion from "./Connexion";


const ConnectUser = () => {
        let history = useHistory();
    return <Connexion historyPath={history} />
}

export default ConnectUser;

