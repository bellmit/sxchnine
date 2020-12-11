import React, {Component} from 'react';
import './OurStory.css';
import story from './ourstory.png';

class OurStory extends Component {

    render() {
        return <div className="Story-Main-div">
            <img src={story} alt="story" className="Story-Image-div"/>
        </div>
    }
}

export default OurStory;