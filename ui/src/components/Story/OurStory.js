import React, {Component} from 'react';
import {isMobile, isTablet, isIPad13} from 'react-device-detect';
import './OurStory.css';
import story from './ourstory.png';
import storyMobile from './story2.jpg';

class OurStory extends Component {

    render() {
        let image = story;

        if (isMobile){
            image = storyMobile
        }

        return <div className="main-div">
            <img src={image} alt="naybxrz econcept store our story vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob" className="Story-Image-div"/>
        </div>
    }
}

export default OurStory;