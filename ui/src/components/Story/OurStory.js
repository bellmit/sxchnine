import React, {Component} from 'react';
import './OurStory.css';
import story from './ourstory.png';
import bottom from './bottom.png';

class OurStory extends Component {

    render() {
        return <div className="main-div">
            <img src={story} alt="story" className="Story-Image-div"/>
            <img src={bottom} alt="naybxrz story" className="Story-Image-bottom"/>
        </div>
    }
}

export default OurStory;