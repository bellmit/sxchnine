import React, {Component} from 'react';
import {isMobile} from 'react-device-detect';
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
            <img src={image} alt="story" className="Story-Image-div"/>
        </div>
    }
}

export default OurStory;