import React from 'react';
import IconButton from 'material-ui/IconButton';
import ExpandMore from 'material-ui/svg-icons/navigation/expand-more';
import ExpandLess from 'material-ui/svg-icons/navigation/expand-less';
import BacklogDeck from "../BacklogDeck/BacklogDeck";
import "./BacklogDeckArea.scss";

class BacklogDeckArea extends React.Component {

  constructor() {
    super();
    this.state = {isVisible: false};

    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    this.setState(prevState => ({
      isVisible: !prevState.isVisible
    }));
  }

  get containerClass() {
     return this.state.isVisible ? "backlog-area" : "backlog-area closed";
  }

  render() {
    const styles = {
      width: 45,
      height: 67,
      padding: 0,
      color: '#fff',
    };
    const iconStyles = {
      width: 45,
      height: 45,
      padding: 0,
      color: '#fff',
    };
    return <div className={this.containerClass}>
      <BacklogDeck />
      <h2>Backlog</h2>
      <IconButton iconStyle={iconStyles} style={styles} onClick={this.handleClick}>
        {this.state.isVisible ? <ExpandLess /> : <ExpandMore />}
      </IconButton>
    </div>
  }

};

export default BacklogDeckArea;
