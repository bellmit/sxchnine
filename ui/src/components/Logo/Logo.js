import React from 'react';
import './Logo.css';
import gotit from '../../images/logo_gotit.png';


const logo = () => {

    return (
        <div>
            <img alt ="" className="Got-it-logo" src={gotit}/>
        </div>
    );
}

export default logo;