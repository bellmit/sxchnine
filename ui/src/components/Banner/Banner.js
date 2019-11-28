import React from "react";
import bannerMen from './rsz_1screen_3.png';
import './Banner.css';

const banner = () => {
    return (
        <div>
            <img alt ="" src={bannerMen} className="Banner-Header-img"/>
            <div className="Banner-Container-Men">
                <p>MEN </p>
            </div>
            <div className="Banner-Empty-Div" />
        </div>
    );
}

export default banner;